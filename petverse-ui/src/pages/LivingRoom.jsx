import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import { Canvas } from '@react-three/fiber';
import { OrbitControls } from '@react-three/drei';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { useLoader } from '@react-three/fiber';
import { useSpring, animated } from '@react-spring/three';
import './LivingRoom.css';
import { postActivity } from '../services/activityService';
import { useEffect } from 'react';
import { fetchAnkaraWeather } from '../services/weatherService';


const PetModel = ({ petType }) => {
    const model = useLoader(GLTFLoader, `/models/${petType}.glb`);
    const [active, setActive] = useState(false);


    const { scale } = useSpring({
        scale: active ? 2.7 : 2.5,
        config: { tension: 300, friction: 10 },
        onRest: () => setActive(false),
    });

    return (
        <animated.primitive
            object={model.scene}
            scale={scale}
            position={[2.7, -1.7, -0.1]}
            rotation={[0, Math.PI * 1.5, 0]}
            onClick={() => setActive(true)}
        />
    );
};

const LivingRoom = () => {
    const location = useLocation();
    const state = location.state || JSON.parse(localStorage.getItem("pet"));

    const { name, species: type } = state || {};
    const [showActivityCard, setShowActivityCard] = useState(false);
    const [selectedActivity, setSelectedActivity] = useState(null);
    const [backgroundImage, setBackgroundImage] = useState('/backgrounds/living-room.png');
    const [mood, setMood] = useState(null); // 🆕 mood state
    const [weather, setWeather] = useState(null);


    if (!name || !type) {
        return <div className="error-screen">No pet data found.</div>;
    }

    const handleActivitySelect = async (activity) => {
        setSelectedActivity(activity);
        setShowActivityCard(false);

        // Arka planı değiştir
        switch (activity) {
            case 'FEED':
                setBackgroundImage('/backgrounds/kitchen.png');
                break;
            case 'WALK':
                setBackgroundImage('/backgrounds/outside.png');
                break;
            case 'PLAY':
                setBackgroundImage('/backgrounds/park.png');
                break;
            case 'SLEEP':
                setBackgroundImage('/backgrounds/bedroom.png');
                break;
            case 'BATH':
                setBackgroundImage('/backgrounds/bathroom.png');
                break;
            default:
                setBackgroundImage('/backgrounds/living-room.png');
        }

        try {
            const userId = localStorage.getItem("userId");
            const rawPetId = state?.id;
            const cleanPetId = rawPetId?.toString().split(":")[0];

            if (!userId || !cleanPetId) {
                console.error("Eksik kullanıcı veya pet bilgisi.");
                return;
            }

            const response = await postActivity({
                type: activity,
                petId: cleanPetId,
                userId
            });

            if (response?.mood) {
                setMood(response.mood);
            }

            console.log(`${activity} aktivitesi başarıyla gönderildi.`);
        } catch (error) {
            console.error("Aktivite gönderimi başarısız:", error);

            // Hata mesajı varsa onu göster
            if (error.message.includes("Yürüyüş iptal") || error.message.includes("Hava")) {
                alert(error.message); // örn: "Yürüyüş iptal: Hava çok sıcak..."
                handleGoHome();       // mutlaka living room'a dön
            } else {
                alert("Aktivite oluşturulamadı. Lütfen tekrar deneyin.");
                handleGoHome(); // hata ne olursa olsun ekranı sıfırla (isteğe bağlı)
            }
        }
    };

    useEffect(() => {
        const getWeather = async () => {
            try {
                const data = await fetchAnkaraWeather();
                setWeather(data);
            } catch (err) {
                console.error("Hava durumu alınamadı:", err);
            }
        };

        getWeather();
    }, []);

    const handleGoHome = () => {
        setBackgroundImage('/backgrounds/living-room.png');
        setShowActivityCard(false);
        setSelectedActivity(null);
    };

    return (
        <>
            <div
                className="living-room-container"
                style={{
                    backgroundImage: `url(${backgroundImage})`,
                    backgroundSize: 'cover',
                    backgroundRepeat: 'no-repeat',
                    backgroundPosition: 'center center',
                    height: '100vh',
                    width: '100vw',
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    zIndex: -1,
                }}
            ></div>

            {/* 🆕 Mood kutucuğu */}
            {mood && (
                <div className="mood-indicator">
                    Mood: <strong>{mood.toUpperCase()}</strong>
                </div>
            )}

            {weather && (
                <div className="weather-box">
                    <div>Sıcaklık: {weather.temperature}°C</div>
                    <div>Rüzgar: {weather.windspeed} km/h</div>
                    <div>Durum: {mapWeatherCode(weather.weathercode)}</div>
                </div>
            )}


            <button
                className="start-activity-button"
                onClick={() => setShowActivityCard(true)}
            >
                Start Activity
            </button>

            {showActivityCard && (
                <div className="activity-modal">
                    <div className="activity-card">
                        <button
                            className="home-button"
                            onClick={handleGoHome}
                        >
                            Home
                        </button>

                        <h3>Select an Activity</h3>
                        <div className="activity-buttons">
                            {["FEED", "WALK", "PLAY", "SLEEP", "BATH"].map((act) => (
                                <button
                                    key={act}
                                    onClick={() => handleActivitySelect(act)}
                                >
                                    {act}
                                </button>
                            ))}
                        </div>
                        <button
                            className="close-button"
                            onClick={() => setShowActivityCard(false)}
                        >
                            Close
                        </button>
                    </div>
                </div>
            )}

            <Canvas className="pet-canvas">
                <ambientLight intensity={0.6} />
                <directionalLight position={[2, 2, 5]} />
                <PetModel petType={type} />
                <OrbitControls
                    makeDefault
                    enableZoom={false}
                    enablePan={false}
                    minAzimuthAngle={-Math.PI / 20}
                    maxAzimuthAngle={Math.PI / 20}
                    minPolarAngle={Math.PI / 2}
                    maxPolarAngle={Math.PI / 2}
                />
            </Canvas>
        </>
    );
};
const mapWeatherCode = (code) => {
    const descriptions = {
        0: "Açık",
        1: "Az bulutlu",
        2: "Parçalı bulutlu",
        3: "Kapalı",
        45: "Sisli",
        61: "Hafif yağmurlu",
        63: "Yağmurlu",
        65: "Yoğun yağmur",
        80: "Sağanak",
        95: "Fırtına"
    };

    return descriptions[code] || "Bilinmiyor";
};

export default LivingRoom;
