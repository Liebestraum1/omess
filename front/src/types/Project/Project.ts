import { AlertContent } from "../common/Alert";

export type Project = {
    projectId: number;
    name: string;
};

export type ProjectInvitationModalProps = {
    open: boolean;
    onClose: () => void;
    showAlert: () => void;
    setAlertContent: (alertCOntent: AlertContent) => void;
};
