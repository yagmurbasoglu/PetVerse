import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "../pages/LoginPage.jsx";

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<Login />} />

            </Routes>
        </BrowserRouter>
    );
}
