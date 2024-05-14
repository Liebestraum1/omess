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
    const {sendMessage, files, resetFile} = useChatStorage();

    const [value, setValue] = useState<string | undefined>('');
    const [isActive, setIsActive] = useState<boolean>(false);
    const [selectedFiles, setSelectedFiles] = useState<Array<File>>([]);
    const [alert, setAlert] = useState<string>('');

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const maxSize = 50 * 1024 * 1024; // 50MB in bytes
        const newFiles: File[] = []
        const selected = event.target.files;
        const sLength = selected == null ? 0 : selected.length;
        setAlert('');
        if (selected && selectedFiles.length + sLength < 10) {
            Array.from(selected).forEach(file => {
                if (file.size > maxSize) {
                    let alertMessage = alert === '' ? '50MB 이상인 파일을 업로드 못했습니다: ' : alert;
                    alertMessage += file.name;
                    setAlert(alertMessage);
                } else {
                    newFiles.push(file)
                }
            });
            setSelectedFiles(prev => [...prev, ...newFiles])
        } else if (selectedFiles.length + sLength > 10) {
            setAlert('파일 업로드는 10 개의 파일로 제한됩니다.')
        }
        event.target.value = ''
    };

    useEffect(() => {
        if (value == null) return;
        if (value!.length >= 1) {
            setIsActive(true);
        } else {
            setIsActive(false);
        }
    }, [value, selectedFiles]);

    const send = () => {
        if (!isActive) return;
        const data = {
            type: 'SEND',
            data: {
                message: value,
                files: files
            }
        }
        sendMessage(JSON.stringify(data))
        setValue('')
        setIsActive(false);
        resetFile();
        setSelectedFiles([]);
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
                    selectedFiles.map((file, idx) => (
                        <ChatFileInput key={idx} file={file} setFiles={setSelectedFiles}/>
                    ))
                }
            </Box>
            <Box flex={1}>
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
            </Box>
            <Box>
                {
                    alert === '' ? null
                        :
                        <p style={{color: '#EB4646'}}>
                            {alert}
                        </p>
                }
            </Box>
        </Box>
    );
}

export default ChatEditorComponent