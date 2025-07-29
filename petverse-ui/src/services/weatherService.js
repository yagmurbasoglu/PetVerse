export const fetchAnkaraWeather = async () => {
    const token = localStorage.getItem("token"); // Token'ı localStorage’dan al

    const response = await fetch("http://localhost:8080/api/weather/ankara", {
        headers: {
            "Authorization": `Bearer ${token}`,
        },
    });

    if (!response.ok) {
        throw new Error("Hava durumu alınamadı.");
    }

    return await response.json(); // { temperature, windspeed, weathercode, ... }
};
