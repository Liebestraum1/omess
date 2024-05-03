import {Box, Button} from "@mui/material";
import {useEffect, useState} from "react";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import MDEditor from '@uiw/react-md-editor';
import SendRoundedIcon from '@mui/icons-material/SendRounded';

const ChatEditorComponent = () => {
    const {sendMessage} = useChatStorage();

    const [value, setValue] = useState<string | undefined>('');
    const [isActive, setIsActive] = useState<boolean>(false);

    useEffect(() => {
        if (value == null || undefined) return;
        if (value!.length >= 1) {
            setIsActive(true);
        }
    }, [value]);

    const send = () => {
        const data = {
            type: 'SEND',
            data: {
                message: value
            }
        }
        sendMessage(JSON.stringify(data))
        setIsActive(false);
        setValue('')
    }
    const handleKeyDown = (event: React.KeyboardEvent<HTMLDivElement>) => {
        if (event.key === 'Enter') {
            event.preventDefault();
            send();
        }
    }
    return (
        <Box p={3}
        >
            <MDEditor data-color-mode="light"
                      value={value}
                      onChange={(v) => setValue(v)}
                      onKeyDown={(e) => handleKeyDown(e)}
                      textareaProps={{
                          placeholder: '채팅 메시지 입력...',
                          maxLength: 16383
                      }}
                      preview={"edit"}
            ></MDEditor>
            <Box
                top={0}
                pt={2}
            >
                <Button color='secondary' variant='contained'
                        disabled={!isActive}
                        onClick={() => send()}
                >
                    <SendRoundedIcon/>
                </Button>
            </Box>
        </Box>
    );
}

export default ChatEditorComponent