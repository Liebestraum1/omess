import {Box, Button} from "@mui/material";
import {ChatMessage} from "../../types/chat/chat.ts";
import {useEffect, useRef, useState} from "react";
import {useChatStorage} from "../../stores/ChatStorage.tsx";

const ChatMessageComponent = (message: ChatMessage) => {
    const {sendMessage} = useChatStorage();
    const [isEditing, setEditing] = useState(false);
    const inputRef = useRef<HTMLInputElement>(null);
    const [inputValue, setInputValue] = useState<string>('')
    const [date, setDate] = useState<string>('');
    const [time, setTime] = useState<string>('');

    useEffect(() => {
        if (isEditing) {
            inputRef.current?.focus();
        }
    }, [isEditing]);

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

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setInputValue(e.target.value);
    };

    const setUpdate = () => {
        setEditing(true);
        setInputValue(message.message);
    }

    const update = (messageId: string, before: string) => {
        const message = inputValue.trim()
        if (message!.length === 0 || message!.length >= 16383) {
            console.error('길이 제한')
            return;
        }

        if (before === message) {
            console.error('변경 내용 없음')
            return;
        }

        const data = {
            type: 'UPDATE',
            data: {
                messageId: messageId,
                message: message
            }
        }

        sendMessage(JSON.stringify(data))
        setEditing(false);
    }

    const deleteMessage = (id: string) => {
        const data = {
            type: 'DELETE',
            data: {
                messageId: id,
            }
        }

        sendMessage(JSON.stringify(data))
    }
    return (
        <Box
            alignItems="center"
            display="flex"
            gap={3}
            padding={2}
        >
            {/*<Avatar*/}
            {/*    src={value.member.}*/}
            {/*/>*/}
            <Box>
                <Box display="flex" gap={2}>
                    <Box sx={{fontWeight: 'bold'}}>{message.member.nickname}</Box>
                    <span>{date + ' ' + time}</span>
                    <span>{message.isUpdated ? '(수정됨)' : ''}</span>
                </Box>
                <Box>
                    {isEditing ? <input
                            ref={inputRef}
                            type="text"
                            value={inputValue}
                            onChange={handleInputChange}
                        /> :
                        <span> {message.message}</span>
                    }
                </Box>
            </Box>
            {!isEditing ?
                <Box>
                    <Button onClick={() => setUpdate()}>메시지 수정</Button>
                    <Button onClick={() => deleteMessage(message.id)}>메시지 삭제</Button>
                </Box>
                :
                <Box>
                    <Button onClick={() => update(message.id, message.message)}>수정</Button>
                    <Button onClick={() => setEditing(false)}>취소</Button>
                </Box>
            }
        </Box>
    );
}

export default ChatMessageComponent;