import {Box, Button} from "@mui/material";
import {useEffect, useState} from "react";
import CloseIcon from '@mui/icons-material/Close';
import SaveSharpIcon from '@mui/icons-material/SaveSharp';
import ImageSharpIcon from '@mui/icons-material/ImageSharp';
import {useChatStorage} from "../../stores/chatStorage.tsx";
import UploadProgressBar from "../Common/UploadProgressBar.tsx";
import {fileCancel, fileUpload} from "../../services/file/FileApi.ts";
import {FileInformation} from "../../types/chat/chat.ts";

const imgFileExt = ['png', 'jpg', 'jpeg', 'gif']

const ChatFileInput = ({file, setFiles}: {
    file: File,
    setFiles: React.Dispatch<React.SetStateAction<Array<File>>>
}) => {
    const [uploading, setUploading] = useState(false);
    const [fileInfo, setFileInfo] = useState<FileInformation | null>();
    const [percent, setPercent] = useState<number | null>(null);
    const {chatId, addFile, removeFile} = useChatStorage();

    const handleRemove = () => {
        if (fileInfo == null || !uploading) return;
        fileCancel(fileInfo.id).then(() => {
                removeFile(fileInfo.id);
                setFiles(prev => prev.filter(value => value.name !== file.name));
            }
        ).catch(e => console.error(e));
    }
    const extractExt = (s: string) => {
        const split = s.split('.');
        return split[split.length - 1];
    }

    useEffect(() => {
        if (uploading) return;
        fileUpload({referenceId: chatId!, referenceType: 'CHAT', files: Array.of(file)}, setPercent)
            .then((response) => {
                const {data} = response;
                addFile({id: data[0].id, address: data[0].address});
                setFileInfo({id: data[0].id, address: data[0].address});
                setUploading(true);
                setPercent(null);
            })
            .catch((e) => {
                console.log(e);
                setFiles(prev => prev.filter(value => value.name !== file.name));
                setPercent(null);
            });
    }, [file]);

    return (
        <>
            <Box
                width={250}
                height={20}
                border={1}
                borderRadius={1}
                pt={2}
                pb={2}
                pl={2}
                my={2}
                display='flex'
                justifyContent='center'
                alignItems='center'
                gap={2}
            >
                {imgFileExt.includes(extractExt(file.name)) ? <ImageSharpIcon/> : <SaveSharpIcon/>}
                <Box
                >
                    <Box
                        sx={{
                            width: 150,
                            whiteSpace: 'nowrap',
                            overflow: 'hidden',
                            textOverflow: 'ellipsis'
                        }}
                    >
                        {file.name}
                    </Box>

                    {
                        !uploading && percent &&
                        <Box mt={2}>
                            <UploadProgressBar value={percent!}/>
                        </Box>
                    }
                </Box>
                <Button
                    color='secondary'
                    sx={{width: '24px', height: '24px'}}
                    onClick={handleRemove}>
                    <CloseIcon/>
                </Button>
            </Box>
        </>
    )
}

export default ChatFileInput;