import { AlertContent } from "../common/Alert";

export type SignUpProps = {
    showAlert: () => void;
    setAlertContent: (alertCOntent: AlertContent) => void;
    onClose: () => void;
};

export type SignUpFormModalProp = {
    open: boolean;
    onClose: () => void;
    showAlert: () => void;
    setAlertContent: (alertCOntent: AlertContent) => void;
};
