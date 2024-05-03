import {Box} from "@mui/material";
import Fab from "@mui/material/Fab";
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import PushPinIcon from '@mui/icons-material/PushPin';

type Props = {
    setIsEditing: (value: any) => void;
    delete: () => void;
}
const ChatMessageMenu = (props: Props) => {

    return (
        <Box
            padding={1}
            boxShadow={3}
            display='flex'
            width="fit-content"
            borderRadius={3}
            gap={2}
        >
            <Fab size="small" color="secondary" aria-label="pin">
                <PushPinIcon/>
            </Fab>
            <Fab size="small" color="secondary" aria-label="edit"
                 onClick={() => props.setIsEditing(true)}
            >
                <EditIcon/>
            </Fab>
            <Fab size="small" color="secondary" aria-label="delete"
                 onClick={() => props.delete()}
            >
                <DeleteIcon/>
            </Fab>
        </Box>
    );
}

export default ChatMessageMenu;