import { Box, Modal } from "@mui/material";
import SignUp from "./SignUp";
import { SignUpFormModalProp } from "../../types/SignUp/SignUp";

const SignUpFormModal = ({ open, onClose, showAlert, setAlertContent }: SignUpFormModalProp) => {
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
                <SignUp onClose={onClose} setAlertContent={setAlertContent} showAlert={showAlert}></SignUp>
            </Box>
        </Modal>
    );
};

export default SignUpFormModal;
