import Box from "@mui/material/Box";
import {Chip} from "@mui/material";
import {useState} from "react";
import DomainModal from "./DomainModal.tsx";



const DomainComponent = (
    {projectId, apiSpecificationId, domainId, name, onChildChange}:
        { projectId: number, apiSpecificationId: number, domainId: number, name: string, onChildChange: () => void}
) => {
    const [open, setOpen] = useState<boolean>(false);

    const handleOpen = () => {
        setOpen(true);
    };

    const changeOpen = (isOpen: boolean) => {
        setOpen(isOpen)
    }

    return (
        <Box
            display="flex"
            marginRight='auto'
            marginBottom='1rem'
        >
            <Chip
                label={name}
                variant="outlined"
                onClick={handleOpen}
            />
            <DomainModal
                projectId={projectId}
                apiSpecificationId={apiSpecificationId}
                domainId={domainId}
                name={name}
                open={open}
                onChildChange={onChildChange}
                changeOpen={changeOpen}
            />

        </Box>
    )
}


export default DomainComponent