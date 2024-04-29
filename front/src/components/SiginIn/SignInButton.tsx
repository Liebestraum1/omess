import { Box, Button } from "@mui/material";
import { useState } from "react";
import SignInFormModal from "./SignInFormModal";

const SignInButton = () => {
    const [open, setOpen] = useState(false);
    const openSinginForm = () => setOpen(true);
    const closeSigninForm = () => setOpen(false);

    return (
        <Box display={"flex"} flexDirection={"column"}>
            <Button
                sx={{
                    marginTop: "16px",
                    fontSize: 16,
                }}
                variant="contained"
                onClick={openSinginForm}
            >
                서버 가입
            </Button>
            <SignInFormModal open={open} onClose={closeSigninForm} />
        </Box>
    );
};

export default SignInButton;
