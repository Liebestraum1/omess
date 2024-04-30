import { Box, Button } from "@mui/material";
import { useState } from "react";
import SignUpFormModal from "./SignUpFormModal";

const SignUpButton = () => {
    const [open, setOpen] = useState(false);
    const openSignUpForm = () => setOpen(true);
    const closeSignUpForm = () => setOpen(false);

    return (
        <Box display={"flex"} flexDirection={"column"}>
            <Button
                sx={{
                    marginTop: "16px",
                    fontSize: 16,
                }}
                variant="contained"
                onClick={openSignUpForm}
            >
                서버 가입
            </Button>
            <SignUpFormModal open={open} onClose={closeSignUpForm} />
        </Box>
    );
};

export default SignUpButton;
