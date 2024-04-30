import { Box, Modal } from "@mui/material";
import SignUp from "./SignUp";

type SignUpFormModalProp = {
    open: boolean;
    onClose: () => void;
};

const SignUpFormModal = ({ open, onClose }: SignUpFormModalProp) => {
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
                <SignUp></SignUp>
            </Box>
        </Modal>
    );
};

export default SignUpFormModal;
