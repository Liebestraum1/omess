import Box from "@mui/material/Box";
import {Chip} from "@mui/material";
const DomainComponent = ({domainId, name}: { domainId: number, name: string }) => {

    return (
        <Box
            display="flex"
            marginRight='auto'
            marginBottom='1rem'
        >
            <Chip
                label={name}
                variant="outlined"
            />
            <Chip label={domainId} variant="outlined"/>
        </Box>
    )
}


export default DomainComponent