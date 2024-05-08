import { AlertColor } from "@mui/material";

export type AlertContent = {
    severity: AlertColor | undefined;
    title: string;
    content: string;
};
