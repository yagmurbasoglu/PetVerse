export default {
    content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
    theme: {
        extend: {
            fontFamily: {
                pixel: ['"Press Start 2P"', 'monospace'],
            },
            boxShadow: {
                neon: "0 0 10px #00ffe7, 0 0 20px #00ffe7, 0 0 30px #00ffe7",
            },
        },
    },
    plugins: [],
}
