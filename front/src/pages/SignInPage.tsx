import { Box, styled } from "@mui/material";
import SignIn from "../components/SignIn/SignIn";

const SignInBox = styled(Box)({
    height: "100vh",
});

const SignInPage = () => {
    return (
        <SignInBox>
            <SignIn></SignIn>
        </SignInBox>
    );
};

export default SignInPage;
