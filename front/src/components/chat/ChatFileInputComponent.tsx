import {Box} from "@mui/material";
import {useState} from "react";
import CloseIcon from '@mui/icons-material/Close';
import SaveSharpIcon from '@mui/icons-material/SaveSharp';
import ImageSharpIcon from '@mui/icons-material/ImageSharp';

const imgFileExt = ['png', 'jpg', 'jpeg', 'gif']

const ChatFileInput = ({file}: { file: File }) => {
    const [isUpload, setIsUpload] = useState(false);

    const extractExt = (s: string) => {
        const split = s.split('.');
        return split[split.length - 1];
    }
    return (
        <Box
            my={2}
            p={2}
            border={1}
            borderRadius={1}
            display='flex'
            alignItems='center'
        >
            {imgFileExt.includes(extractExt(file.name)) ? <ImageSharpIcon/> : <SaveSharpIcon/>}
            <Box
                sx={{
                    width: 150,
                    whiteSpace: 'nowrap',
                    overflow: 'hidden',
                    textOverflow: 'ellipsis'
                }}
            >
                { file.name }
            </Box>
            <CloseIcon/>
        </Box>
    )
}

export default ChatFileInput;