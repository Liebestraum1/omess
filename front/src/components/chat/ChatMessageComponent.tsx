import {Box} from "@mui/material";
import {useEffect, useRef, useState} from "react";
import ChatMessageMenu from "./ChatMessageMenu.tsx";
import ChatEditingButton from "./ChatEditingButton.tsx";
import TextField from "@mui/material/TextField";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import MDEditor from '@uiw/react-md-editor';
import '../../styles/MdEditor.css'
import {ChatMessage} from "../../types/chat/chat.ts";

type Props = {
    prevMessage: ChatMessage,
    message: ChatMessage
}
const ChatMessageComponent = (props: Props) => {
    const {sendMessage} = useChatStorage();
    const [isEditing, setIsEditing] = useState(false);
    const [isView, setIsView] = useState<boolean>(false);
    const [nowTime, setNowTime] = useState<string>('');
    const inputRef = useRef<HTMLInputElement>(null);
    const [inputValue, setInputValue] = useState<string>(props.message.message)


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
        const dateTime = props.message.createAt.split(' ')
        const t = dateTime[1].split(':');

        const res = transTime(t);

        setNowTime(res);
    }, [props.message.message]);

    useEffect(() => {
        if (props.prevMessage == null) return;
        const dateTime = props.prevMessage.createAt.split(' ')

    }, [props.prevMessage?.message]);

    const handleInputChange = (e: any) => {
        setInputValue(e.target.value);
    };

    const update = () => {
        const before = props.message.message;
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
                messageId: props.message.id,
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
                messageId: props.message.id,
            }
        }

        sendMessage(JSON.stringify(data))
    }

    const pinMessage = () => {
        const data = {
            type: 'PIN',
            data: {
                messageId: props.message.id,
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
            className='view-box'
            sx={{
                '&:hover': {
                    backgroundColor: 'grey.100',
                }
            }}
            onMouseEnter={() => setIsView(true)}  // 마우스가 컴포넌트 위로 올라오면 상태를 true로 설정
            onMouseLeave={() => setIsView(false)}
        >
            {/*<Avatar*/}
            {/*    src={value.member.}*/}
            {/*/>*/}
            {
                !isEditing ?
                    <Box
                        sx={props.message.classify !== 'USER' ? {color: 'grey.500'} : null}
                    >
                        <Box display="flex" gap={2}>
                            {
                                props.prevMessage == null || props.message.member.id !== props.prevMessage?.member.id ?
                                    <Box sx={{fontWeight: 'bold'}}>{props.message.member.nickname}</Box> : null
                            }
                            <Box>
                                <span>{nowTime}</span>
                                <span
                                    style={{fontSize: '12px'}}>{props.message.classify !== 'USER' ? '(시스템 메시지)' : null}</span>
                            </Box>
                        </Box>
                        <Box>
                            <MDEditor.Markdown
                                className='markdown-view'
                                source={props.message.isUpdated ? props.message.message + ' (수정됨) ' : props.message.message}/>
                        </Box>
                    </Box> :
                    <TextField
                        ref={inputRef}
                        value={inputValue}
                        onChange={(e) => handleInputChange(e)}
                        label="메시지" sx={{width: '40%'}}/>
            }
            <Box>
                {
                    props.message.classify === 'USER' ? !isEditing ?
                            isView ?
                                <ChatMessageMenu isPined={props.message.isPined} pin={pinMessage}
                                                 setIsEditing={setIsEditing}
                                                 delete={deleteMessage}/>
                                :
                                null
                            :
                            <ChatEditingButton update={update} setIsEditing={setIsEditing}/>
                        : null
                }
            </Box>
        </Box>
    );
}

export default ChatMessageComponent;