import {Box, LinearProgress} from "@mui/material";
import {LinearProgressProps} from "@mui/material/LinearProgress/LinearProgress";

const UploadProgressBar = (props: LinearProgressProps & { value: number }) => {
    return (
        <Box sx={{width: '100%'}}>
            <Box sx={{display: 'flex', alignItems: 'center'}}>
                <Box sx={{width: '100%', mr: 1}}>
                    <LinearProgress variant="determinate"  {...props}/>
                </Box>
            </Box>
        </Box>
    );
}

export default UploadProgressBar;