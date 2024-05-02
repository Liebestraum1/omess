import { Box, styled } from "@mui/material";
import SignIn from "../components/SignIn/SignIn";
import { useSignInStore } from "../stores/SignInStorage";

const SignInBox = styled(Box)({
    height: "100vh",
});

const SignInPage = () => {
    const { memberNickname } = useSignInStore();
    console.log(memberNickname);
    return (
        <SignInBox>
            <SignIn></SignIn>
        </SignInBox>
    );
};

export default SignInPage;
