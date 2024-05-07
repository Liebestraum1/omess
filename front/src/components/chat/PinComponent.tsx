import {Box, Button} from "@mui/material";
import PushPinIcon from "@mui/icons-material/PushPin";

type Props = {
    pinCount: number
    setIsOpened: any
    setSelectedTab:any
}
const PinComponent = (props: Props) => {
    const handleSelectedTab = () => {
        props.setIsOpened(true);
        props.setSelectedTab('pin')
    }
    return (
        <Button
            color='secondary'
            onClick={handleSelectedTab}
        >
            <Box display='flex'
                 gap={2}
                 alignItems='center'
            >
                <PushPinIcon/>
                <span>{props.pinCount}</span>
            </Box>
        </Button>
    );
}

export default PinComponent;