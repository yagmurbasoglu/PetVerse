import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./RegisterPage.css";
import { registerUser } from "../services/auth";

const RegisterPage = () => {
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const handleSignup = async () => {
        try {
            const token = await registerUser({ email, username, password });
            console.log("Kayıt başarılı, token:", token);
            localStorage.setItem("token", token); // istersen saklayabilirsin
            navigate("/login"); // kayıt sonrası giriş sayfasına yönlendir
        } catch (err) {
            console.error("Register failed:", err);
            alert("Register failed: " + err.message);
        }
    };

    return (
        <div className="register-container">
            <div className="register-card">
                <div className="logo-title">
                    <img src="/backgrounds/cat-logo.png" alt="PetVerse Logo" className="cat-logo" />
                    <div className="logo-text">PETVERSE</div>
                </div>
                <h1>REGISTER</h1>

                <div className="input-group">
                    <label htmlFor="email">EMAIL</label>
                    <input
                        type="email"
                        id="email"
                        placeholder="Enter your email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                </div>

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
                    <button onClick={handleSignup}>SIGN UP</button>
                </div>
            </div>
        </div>
    );
};

export default RegisterPage;
