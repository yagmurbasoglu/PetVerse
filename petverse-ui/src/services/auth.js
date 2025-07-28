import { jwtDecode } from "jwt-decode"; // ✅ doğru kullanım


export const loginUser = async ({ username, password }) => {
    const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
    });

    if (!response.ok) {
        throw new Error("Login failed");
    }

    const token = await response.text();
    const decoded = jwtDecode(token);

    // userId'yi localStorage'a yaz
    localStorage.setItem("token", token);
    localStorage.setItem("userId", decoded.userId); // backend'de claim 'userId' ise

    return token;
};

export const registerUser = async ({ username, password, email }) => {
    const response = await fetch("http://localhost:8080/api/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password, email }),
    });

    if (!response.ok) {
        throw new Error("Register failed");
    }

    const token = await response.text();
    const decoded = jwtDecode(token);

    // userId'yi localStorage'a yaz
    localStorage.setItem("token", token);
    localStorage.setItem("userId", decoded.userId);

    return token;
};
