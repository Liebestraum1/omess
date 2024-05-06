import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react";

type Mode = "development" | "production";

export default ({ mode }: { mode: Mode }) => {
    const env = loadEnv(mode, process.cwd(), "");
    return defineConfig(
        // 하기 코드는 mode에 관계없이 .env의 설정을 이용함
        {
            plugins: [react()],
            server: {
                proxy: {
                    "/api": {
                        target: env.VITE_SERVER_URL,
                        changeOrigin: true,
                    },
                },
            },
            define: {
                'global': {},
            },
        }
    );
};
