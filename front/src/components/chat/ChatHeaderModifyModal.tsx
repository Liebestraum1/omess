import {Backdrop, Box, Button, Fade, Modal} from "@mui/material";
import Textarea from "@mui/joy/Textarea";
import {useChatStorage} from "../../stores/chatStorage.tsx";
import {useRef} from "react";

const style = {
    position: 'absolute',
    top: '25%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: '50vw',
    height: '40vh',
    backgroundColor: 'background.paper',
    borderRadius: 2,
    boxShadow: 24,
};
type Props = {
    open: boolean,
    setOpen: any
}
const ChatHeaderModifyModal = (props: Props) => {
    const {sendMessage} = useChatStorage();
    const detail = useRef<HTMLTextAreaElement>(null);

    const modifyHeader = () => {
        const d = detail.current?.value;
        const data = {
            type: 'HEADER',
            data: {
                detail: d
            }
        }
        sendMessage(JSON.stringify(data))
        props.setOpen(false)
    }
    const handleClose = () => props.setOpen(false);
    return (
        <Modal
            aria-labelledby="transition-modal-title"
            aria-describedby="transition-modal-description"
            open={props.open}
            onClose={handleClose}
            closeAfterTransition
            slots={{backdrop: Backdrop}}
            slotProps={{
                backdrop: {
                    timeout: 500,
                },
            }}
        >
            <Fade in={props.open}>
                <Box sx={style} overflow='hidden'>
                    {/* 헤더 */}
                    <Box p={2}
                         sx={{
                             backgroundColor: '#4F378B'
                         }}
                    >
                        <span style={{color: 'white'}}>채널 헤더 변경</span>
                    </Box>
                    {/* 바디 */}
                    <Box p={2}
                         display='flex'
                         flexDirection='column'
                         justifyContent='space-between'
                    >
                        <Box mb={3}>
                            <p>상단 채널 이름 옆에 표시될 글자를 편집하세요.</p>
                        </Box>
                        <Textarea
                            slotProps={{textarea: {ref: detail}}}
                            color="neutral"
                            disabled={false}
                            minRows={4}
                            placeholder="채널 헤더 변경.."
                            size="lg"
                            variant="outlined"
                        />
                        <Box
                            display='flex'
                            justifyContent='end'
                            mt={3}
                            gap={3}
                        >
                            <Button color='secondary' variant='contained'
                                    onClick={() => modifyHeader()}
                            >변경</Button>
                            <Button color='secondary' variant='contained'
                                    onClick={() => handleClose()}
                            >취소</Button>
                        </Box>
                    </Box>
                </Box>
            </Fade>
        </Modal>
    );
}

export default ChatHeaderModifyModal;