import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './PetSelectionPage.css';
import { createPet, fetchUserPets } from "../services/pet";

const PetSelectionPage = () => {
    const [selectedPet, setSelectedPet] = useState(null);
    const [petName, setPetName] = useState('');
    const [showNameInput, setShowNameInput] = useState(false);
    const navigate = useNavigate();

    const petSettings = {
        cat: {
            position: [2.5, -1.3, -0.1],
            rotation: [0, -Math.PI / 2, 0],
            scale: 2.5,
        },
        dog: {
            position: [2.5, -1.25, -0.1],
            rotation: [0, -Math.PI / 2, 0],
            scale: 2.5,
        },
        bird: {
            position: [2.5, -1.1, 0],
            rotation: [0, -Math.PI / 2, 0],
            scale: 2.2,
        },
    };

    const handleSelect = async (petType) => {
        setSelectedPet(petType);
        setPetName('');
        setShowNameInput(false);

        const userId = localStorage.getItem("userId");
        const token = localStorage.getItem("token");

        try {
            const pets = await fetchUserPets(userId, token);
            const existingPet = pets.find(p => p.species.toLowerCase() === petType.toLowerCase());

            if (existingPet) {
                const petData = {
                    ...existingPet,
                    ...petSettings[petType],
                };
                localStorage.setItem("pet", JSON.stringify(petData));
                navigate("/living-room", { state: petData });
            } else {
                setShowNameInput(true);
            }
        } catch (err) {
            console.error("Pet kontrol hatası:", err);
            alert("An error occurred. Please try again.");
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const userId = localStorage.getItem("userId");
        const token = localStorage.getItem("token");

        try {
            const createdPet = await createPet(
                {
                    name: petName,
                    species: selectedPet.toUpperCase(),
                    age: 0,
                },
                userId,
                token
            );

            const petData = {
                ...createdPet,
                ...petSettings[selectedPet],
            };

            localStorage.setItem("pet", JSON.stringify(petData));
            navigate("/living-room", { state: petData });
        } catch (error) {
            console.error("Pet creation error:", error);
            alert("Pet could not be created. Please try again.");
        }
    };

    return (
        <div className="pet-selection-bg">
            <div className="pet-selection-card">
                <h2>Select Your Pet</h2>
                <div className="pet-buttons">
                    <button onClick={() => handleSelect("cat")}>🐱 Cat</button>
                    <button onClick={() => handleSelect("dog")}>🐶 Dog</button>
                    <button onClick={() => handleSelect("bird")}>🐦 Bird</button>
                </div>

                {showNameInput && (
                    <form onSubmit={handleSubmit} className="pet-form">
                        <label htmlFor="petName">
                            Enter a name for your {selectedPet}:
                        </label>
                        <input
                            type="text"
                            id="petName"
                            placeholder="Your pet's name"
                            value={petName}
                            onChange={(e) => setPetName(e.target.value)}
                            required
                        />
                        <button type="submit">Create Pet</button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default PetSelectionPage;
