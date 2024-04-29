import { Box, Modal } from "@mui/material";
import SignIn from "./SignIn";

type SingInFormModalProp = {
    open: boolean;
    onClose: () => any;
};

const SignInFormModal = ({ open, onClose }: SingInFormModalProp) => {
    return (
        <Modal
            open={open}
            onClose={onClose}
            sx={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
            }}
        >
            <Box>
                <SignIn></SignIn>
            </Box>
        </Modal>
    );
};

export default SignInFormModal;
