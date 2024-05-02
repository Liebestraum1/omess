import {ChatMessage} from "../../types/chat/chat.ts";

const ChatHeaderDataComponent = ({header}: { header: ChatMessage | undefined }) => {
    return (
        <>
            <span>{header?.message}</span>
        </>
    );
}

export default ChatHeaderDataComponent;