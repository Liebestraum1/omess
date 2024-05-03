import {Button} from "@mui/material";
import {useState} from "react";
import ChatHeaderModifyModal from "./ChatHeaderModifyModal.tsx";


const ChatHeaderDataComponent = ({header}: { header: string | undefined }) => {
    const [open, setOpen] = useState(false);
    const handleOpen = () => setOpen(true);
    return (
        <>
            {header ?
                <span>{header}</span>
                :
                <Button color="secondary"
                        onClick={() => handleOpen()}
                >
                    <span>채널 설명 추가하기</span>
                </Button>
            }
            <ChatHeaderModifyModal open={open} setOpen={setOpen}/>
        </>
    );
}

export default ChatHeaderDataComponent;