import { Button } from "@mui/material";
import { useLoginStore } from "../../stores/LoginStorage";
import { useNavigate } from "react-router-dom";
import { LoginStatusProps } from "../../types/Login/LoginProps";
import { useCallback, useEffect, useState } from "react";

const LoginButton = ({ loginStatus }: { loginStatus: LoginStatusProps }) => {
    const navigate = useNavigate();
    const { setServerLogin, setUserLogin } = useLoginStore();

    const [buttonText, setButtonText] = useState<string>("");
    const [buttonFunction, setButtonFunction] = useState<() => void>(() => undefined);

    const updateButtonState = useCallback(
        (loginStatus: LoginStatusProps) => {
            if (loginStatus === "none") {
                setButtonText("서버 접속");
                setButtonFunction(() => setServerLogin);
            } else if (loginStatus === "server") {
                setButtonText("로그인");
                setButtonFunction(() => () => {
                    setUserLogin();
                    navigate("/main");
                });
            } else {
                setButtonFunction(() => () => {
                    navigate("/main");
                });
            }
        },
        [navigate, setServerLogin, setUserLogin]
    );

    useEffect(() => {
        updateButtonState(loginStatus);
    }, [loginStatus, updateButtonState]);

    return (
        <Button
            sx={{
                marginTop: "36px",
                fontSize: 16,
            }}
            variant="contained"
            onClick={buttonFunction}
        >
            {buttonText}
        </Button>
    );
};

export default LoginButton;
