import { Button } from "@mui/material";
import { LoginStatusProps } from "../../types/Login/LoginProps";
import { useCallback, useEffect, useState } from "react";

const LoginFormSubmitButton = ({ loginStatus }: { loginStatus: LoginStatusProps }) => {
    const [buttonText, setButtonText] = useState<string>("");

    const updateButtonState = useCallback((loginStatus: LoginStatusProps) => {
        if (loginStatus === "none") {
            setButtonText("서버 접속");
        } else {
            setButtonText("로그인");
        }
    }, []);

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
            type="submit"
        >
            {buttonText}
        </Button>
    );
};

export default LoginFormSubmitButton;
