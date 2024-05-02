import {Box} from "@mui/material";
import ChatMessageComponent from "./ChatMessageComponent.tsx";
import {useInView} from "react-intersection-observer";
import {useEffect, useRef} from "react";
import {useChatStorage} from "../../stores/chatStorage.tsx";

const ChatHistoryComponent = () => {
    const {messages, sendMessage} = useChatStorage();
    const scrollRef = useRef<HTMLDivElement>(null);

    const {ref, inView} = useInView({
        threshold: 0,
        triggerOnce: false
    })


    // 스크롤이 상단에 도달했을 때 추가 메시지 로드
    useEffect(() => {
        if (inView) {
            sendMessage(JSON.stringify({
                type: 'LOAD',
                data: {
                    offset: messages?.length
                }
            }));
        }
    }, [inView]);

    return (
        <Box
            ref={scrollRef}
            sx={{
                height: '65vh',
                overflowY: 'scroll',
                display: 'flex',
                flexDirection: 'column-reverse',
                p: 2
            }}
        >
            {messages ? messages!.map((message) => (
                <ChatMessageComponent key={message.id} {...message} />
            )) : null}
            <div ref={ref}></div>
        </Box>
    );
}

export default ChatHistoryComponent;