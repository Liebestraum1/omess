import Box from "@mui/material/Box";
import SignInLogo from "./SignInLogo";
import SignInTitle from "./SignInTitle";
import SignInForm from "./SignInForm";
import { useSignInStore } from "../../stores/SignInStorage";
import { styled } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

const SignInBox = styled(Box)({
    width: "100%",
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
});

const SignIn = () => {
    const { signInStatus } = useSignInStore();
    const navigate = useNavigate();

    useEffect(() => {
        if (signInStatus === "member") {
            navigate("/main");
        }
    }, [signInStatus]);
    return (
        <SignInBox>
            <SignInLogo />
            <SignInTitle signInStatus={signInStatus}></SignInTitle>
            <SignInForm signInStatus={signInStatus}></SignInForm>
        </SignInBox>
    );
};

export default SignIn;
