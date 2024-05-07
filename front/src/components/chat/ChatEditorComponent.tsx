import {Box, Button} from "@mui/material";
import {useEffect, useState} from "react";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import MDEditor from '@uiw/react-md-editor';
import SendRoundedIcon from '@mui/icons-material/SendRounded';
import AttachFileRoundedIcon from '@mui/icons-material/AttachFileRounded';
import '../../styles/MdEditor.css'
import {customCommands, VisuallyHiddenInput} from "./EditorAttr.ts";
import ChatFileInput from "./ChatFileInputComponent.tsx";


const ChatEditorComponent = () => {
    const {sendMessage} = useChatStorage();

    const [value, setValue] = useState<string | undefined>(undefined);
    const [isActive, setIsActive] = useState<boolean>(false);
    const [files, setFiles] = useState<Array<File>>([]);
    const [alert, setAlert] = useState<string>('');

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const maxSize = 50 * 1024 * 1024; // 50MB in bytes
        const newFiles: File[] = []
        const selectedFiles = event.target.files;
        setAlert('');
        if (selectedFiles) {
            Array.from(selectedFiles).forEach(file => {
                if (file.size > maxSize) {
                    setAlert(prev => prev + file.name + ' ');
                } else {
                    newFiles.push(file)
                }
            });
            setFiles(prev => [...prev, ...newFiles])
        }
    };

    useEffect(() => {
        if (value == null || undefined) return;
        if (value!.length >= 1) {
            setIsActive(true);
        } else {
            setIsActive(false);
        }
    }, [value]);

    const send = () => {
        if (!isActive) return;
        const data = {
            type: 'SEND',
            data: {
                message: value
            }
        }
        setValue('')
        sendMessage(JSON.stringify(data))
        setIsActive(false);
    }
    const handleKeyDown = (event: React.KeyboardEvent<HTMLDivElement>) => {
        if (event.key === 'Enter' && !event.shiftKey && !event.altKey && !event.ctrlKey && !event.metaKey) { //shift + enter, alt + enter?? x + enter 면 다됨 난 그냥 엔터만
            send();
            event.preventDefault();
        }
    }


    const sendBtn = {
        name: 'send',
        keyCommand: 'send',
        icon: (
            <Box display='flex'
                 alignItems='center'
                 p={1}
            >
                <Button
                    className='file-btn'
                    component="label"
                    role={undefined}
                    variant="contained"
                    tabIndex={-1}
                >
                    <AttachFileRoundedIcon/>
                    <VisuallyHiddenInput type="file"
                                         multiple={true}
                                         onChange={handleFileChange}
                    />
                </Button>
                <Button
                    disabled={!isActive}
                    onClick={() => send()}
                    className={isActive ? 'send-btn active' : 'send-btn deactive'}
                >
                    <SendRoundedIcon className='send-icon'/>
                </Button>
            </Box>
        ),
    };
    return (
        <Box p={3}
        >
            <Box
                display='flex'
                gap={2}
                overflow='auto'
            >
                {
                    files.map((file) => (
                        <ChatFileInput file={file}/>
                    ))
                }
            </Box>
            <MDEditor
                className='my-editor'
                data-color-mode="light"
                value={value}
                onChange={(v) => setValue(v)}
                onKeyPress={handleKeyDown}
                textareaProps={{
                    placeholder: '메시지 입력...',
                    maxLength: 16383
                }}
                height='100%'
                visibleDragbar={false}
                preview={"edit"}
                toolbarBottom={true}
                commands={customCommands}
                extraCommands={[
                    sendBtn
                ]}
            ></MDEditor>
            <Box>
                {
                    alert === '' ? null
                        :
                        <p style={{color: '#EB4646'}}>
                            50MB 이상인 파일을 업로드 못했습니다: {alert}
                        </p>
                }
            </Box>
        </Box>
    );
}

export default ChatEditorComponent