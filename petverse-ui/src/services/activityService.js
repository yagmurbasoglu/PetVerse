export const postActivity = async ({ type, petId, userId }) => {
    const token = localStorage.getItem("token");
    const safeUserId = parseInt(userId);

    const response = await fetch(
        `http://localhost:8080/api/activities/${type.toLowerCase()}?petId=${petId}`,
        {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`,
                "X-User-Id": safeUserId
            },
            body: null
        }
    );

    if (!response.ok) {
        const errorText = await response.text(); // 🔥 Hata mesajını al
        throw new Error(errorText || "Aktivite başarısız oldu.");
    }

    return await response.json();
};
