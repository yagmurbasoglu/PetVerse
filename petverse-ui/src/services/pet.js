// Yeni pet oluşturur
export const createPet = async (petData, userId, token) => {
    const response = await fetch("http://localhost:8080/api/pets", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`,
            "X-User-Id": userId
        },
        body: JSON.stringify(petData)
    });

    if (!response.ok) {
        throw new Error("Pet creation failed");
    }

    return response.json();
};

// Kullanıcının tüm pet'lerini getirir
export const fetchUserPets = async (userId, token) => {
    const response = await fetch(`http://localhost:8080/api/pets?userId=${userId}`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${token}`,
            "X-User-Id": userId
        },
    });

    if (!response.ok) {
        throw new Error("Pet listesi alınamadı");
    }

    return await response.json();
};
