import client from "../common";
import {AxiosProgressEvent} from "axios";

type Props = {
    referenceType: string,
    referenceId: string,
    files: Array<File>
}
export const fileUpload = async (props: Props, onProgress: (percent: number) => void) => {
    const form = new FormData();
    form.append('referenceId', props.referenceId);
    form.append('referenceType', props.referenceType);
    props.files.forEach(value => form.append('files', value));

    return await client.post('/api/v1/files', form, {
        headers: {
            'Content-Type': 'multipart/form-data'
        },
        onUploadProgress: (progressEvent: AxiosProgressEvent) => {
            const total = progressEvent.total ?? 1;
            const percentCompleted = Math.round((progressEvent.loaded * 100) / total);
            onProgress(percentCompleted);
        }
    });
};

export const fileCancel = async (fileId: number)=> {
    return await client.delete('/api/v1/files', {
        params: {
            id: fileId
        },
        headers: {
            'Content-Type': 'application/json'
        }
    });
}