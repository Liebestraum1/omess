import { Button } from "@mui/material";
import { SignInStatus } from "../../types/SignIn/SignIn";
import { useCallback, useEffect, useState } from "react";

const SignInFormSubmitButton = ({ signInStatus }: { signInStatus: SignInStatus }) => {
    const [buttonText, setButtonText] = useState<string>("");

    const updateButtonState = useCallback((signInStatus: SignInStatus) => {
        if (signInStatus === "none") {
            setButtonText("서버 접속");
        } else {
            setButtonText("로그인");
        }
    }, []);

    useEffect(() => {
        updateButtonState(signInStatus);
    }, [signInStatus, updateButtonState]);

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

export default SignInFormSubmitButton;
