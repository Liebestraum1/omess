export type LoginStatusProps = "none" | "server" | "user";

export type LoginFormInputProp = {
    type: string;
    label: string;
    helperText: string;
    onFormData: (formData: string) => void;
};
