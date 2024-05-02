import {Box} from "@mui/material";
import {ChatMessage} from "../../types/chat/chat.ts";
import {useEffect, useRef, useState} from "react";
import ChatMessageMenu from "./ChatMessageMenu.tsx";
import ChatMessageEditing from "./ChatMessageEditing.tsx";
import TextField from "@mui/material/TextField";
import {useChatStorage} from "../../stores/chatStorage.tsx";

const ChatMessageComponent = (message: ChatMessage) => {
    const {sendMessage} = useChatStorage();
    const [isEditing, setIsEditing] = useState(false);
    const [date, setDate] = useState<string>('');
    const [time, setTime] = useState<string>('');
    const inputRef = useRef<HTMLInputElement>(null);
    const [inputValue, setInputValue] = useState<string>(message.message)


    function transTime(t: string[]) {
        const str = (parseInt(t[0]) / 12) < 1 ? '오전 ' : '오후 ';

        t[0] = ((parseInt(t[0]) % 12) || 12).toString();
        t[2] = parseInt(t[2]).toString();
        t[0] = t[0].toString().padStart(2, '0');
        t[1] = t[1].toString().padStart(2, '0');
        if (t.length > 2) { // 초가 포함되어 있다면
            t[2] = t[2].toString().padStart(2, '0');
        }

        return str + t.join(':');
    }

    useEffect(() => {
        const dateTime = message.createAt.split(' ')
        const t = dateTime[1].split(':');

        const res = transTime(t);

        setDate(dateTime[0]);
        setTime(res);
    }, [message]);

    const handleInputChange = (e: any) => {
        setInputValue(e.target.value);
    };

    const update = () => {
        const before = message.message;
        const m = inputValue.trim()
        if (m!.length === 0 || m!.length >= 16383) {
            console.error('길이 제한')
            return;
        }

        if (before === m) {
            console.error('변경 내용 없음')
            return;
        }

        const data = {
            type: 'UPDATE',
            data: {
                messageId: message.id,
                message: m
            }
        }

        sendMessage(JSON.stringify(data))
        setIsEditing(false);
    }

    const deleteMessage = () => {
        const data = {
            type: 'DELETE',
            data: {
                messageId: message.id,
            }
        }

        sendMessage(JSON.stringify(data))
    }


    return (
        <Box
            alignItems="center"
            display="flex"
            justifyContent="space-between"
            gap={3}
            padding={2}
        >
            {/*<Avatar*/}
            {/*    src={value.member.}*/}
            {/*/>*/}
            {
                !isEditing ?
                    <Box>
                        <Box display="flex" gap={2}>
                            <Box sx={{fontWeight: 'bold'}}>{message.member.nickname}</Box>
                            <span>{date + ' ' + time}</span>
                            <span>{message.isUpdated ? '(수정됨)' : ''}</span>
                        </Box>
                        <Box>{message.message}</Box>
                    </Box> :
                    <TextField
                        ref={inputRef}
                        value={inputValue}
                        onChange={(e) => handleInputChange(e)}
                        label="메시지" sx={{width: '40%'}}/>
            }
            {!isEditing ?
                <ChatMessageMenu setIsEditing={setIsEditing} delete={deleteMessage}/>
                :
                <ChatMessageEditing update={update} setIsEditing={setIsEditing}/>
            }
        </Box>
    );
}

export default ChatMessageComponent;