import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./LoginPage.css";
import { loginUser } from "../services/auth";
import { jwtDecode } from "jwt-decode";



const LoginPage = () => {
    const navigate = useNavigate();

    // ✅ Giriş için form state'leri
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    // ✅ Giriş işlemi
    const handleLogin = async () => {
        try {
            const token = await loginUser({ username, password });
            console.log("Giriş başarılı, token:", token);

            // ✅ JWT içinden userId'yi çek
            const decoded = jwtDecode(token);
            const userId = parseInt(decoded.userId);
            console.log("Çözümlenmiş token:", decoded);

            // ✅ localStorage'a kaydet
            localStorage.setItem("token", token);
            localStorage.setItem("userId", userId); // ❗ frontend'te kullanılıyor

            navigate("/select-pet");
        } catch (error) {
            console.error("Giriş başarısız:", error);
            alert("Login failed. Please check your credentials.");
        }
    };


    return (
        <div className="login-container">
            <div className="login-card">
                <div className="logo-title">
                    <img src="/backgrounds/cat-logo.png" alt="PetVerse Logo" className="cat-logo" />
                    <div className="logo-text">PETVERSE</div>
                </div>
                <h1>WELCOME</h1>

                <div className="input-group">
                    <label htmlFor="username">USERNAME</label>
                    <input
                        type="text"
                        id="username"
                        placeholder="Enter your username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>

                <div className="input-group">
                    <label htmlFor="password">PASSWORD</label>
                    <input
                        type="password"
                        id="password"
                        placeholder="Enter your password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>

                <div className="buttons">
                    <button onClick={handleLogin}>PLAY</button>
                    <button onClick={() => navigate("/register")}>GUEST?</button>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
