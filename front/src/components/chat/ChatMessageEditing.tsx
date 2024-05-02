import {Box, Button} from "@mui/material";

type Props = {
    setIsEditing: (value: any) => void;
    update: () => void;
}
const ChatMessageEditing = (props: Props) => {
    return (
        <Box
            display="flex"
            gap={2}
        >
            <Button
                onClick={() => props.update()}
                variant="contained" color="secondary" size="small">
                수정
            </Button>
            <Button
                onClick={() => props.setIsEditing(false)}
                variant="contained"
                color="secondary"
                size="small">
                취소
            </Button>
        </Box>)
}

export default ChatMessageEditing