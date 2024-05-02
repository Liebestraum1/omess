import {Box} from "@mui/material";
import {useRef} from "react";
import {useChatStorage} from "../../stores/ChatStorage.tsx";

const ChatEditorComponent = () => {
    const { sendMessage } = useChatStorage();
    const message = useRef<HTMLInputElement>(null);

    const send = () => {
        const data = {
            type: 'SEND',
            data: {
                message: 'test'
            }
        }
        sendMessage(JSON.stringify(data))
    }
    return (
        <Box style={{height: '15vh'}}>
            <input ref={message} type="text" placeholder="내용을 입력해 주세요."/>
            <button onClick={() => send()}>보내기</button>
        </Box>
    );
}

export default ChatEditorComponent