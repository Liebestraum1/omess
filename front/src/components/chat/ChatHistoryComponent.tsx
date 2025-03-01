import {Box, Divider} from "@mui/material";
import ChatMessageComponent from "./ChatMessageComponent.tsx";
import {useInView} from "react-intersection-observer";
import {useEffect, useRef} from "react";
import {useChatStorage} from "../../stores/chatStorage.tsx";

const ChatHistoryComponent = () => {
    const {messages, sendMessage, isEnter} = useChatStorage();
    const scrollRef = useRef<HTMLDivElement>(null);

    const {ref, inView} = useInView({
        threshold: 0,
        triggerOnce: false
    })


    // 스크롤이 상단에 도달했을 때 추가 메시지 로드
    useEffect(() => {
        if (inView && isEnter) {
            sendMessage(JSON.stringify({
                type: 'LOAD',
                data: {
                    offset: messages?.length
                }
            }));
        }
    }, [inView, isEnter]);

    return (
        <Box
            ref={scrollRef}
            sx={{
                overflowY: 'scroll',
                display: 'flex',
                flexDirection: 'column-reverse',
                px: 2
            }}
        >
            {messages ? messages!.map((message, idx) => (
                <Box key={idx}>
                    {
                        messages[idx + 1] == null || message.createAt.split(' ')[0] !== messages[idx + 1].createAt.split(' ')[0] ?
                            <Divider>{message.createAt.split(' ')[0]}</Divider> : null
                    }
                    <ChatMessageComponent key={message.id} message={message} prevMessage={messages[idx + 1]}/>
                </Box>
            )) : null}
            <Box ref={ref} p={1}></Box>
        </Box>
    );
}

export default ChatHistoryComponent;