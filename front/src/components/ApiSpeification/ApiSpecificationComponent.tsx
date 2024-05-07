import {useEffect, useState} from "react";
import {Box, Chip, List, ListItem} from "@mui/material";
import {loadApiSpecification} from "./request/ApiSpecificationRequest.ts";
import {ApiSpecification} from "../../types/api-specification/ApiSpecification.ts";
import ApiListComponent from "./api/ApiListComponent.tsx";
import DomainComponent from "./domain/DomainComponent.tsx";
import AddCircleIcon from '@mui/icons-material/AddCircle';
import CreateDomainModal from "./domain/CreateDomainModal.tsx";
import ApiCreateModal from "./api/ApiCreateModal.tsx";

const ApiSpecificationComponent = ({projectId, apiSpecificationId}: {
    projectId: number,
    apiSpecificationId: number
}) => {
    const [ApiSpecification, setApiSpecification] = useState<ApiSpecification | null>(null);
    const [isNewDomainMode, setIsNewDomainMode] = useState<boolean>(false)
    const [isNewApiMode, setIsNewApiMode] = useState<boolean>(false)
    const fetchApiSpecification = async () => {
        const data = await loadApiSpecification(projectId, apiSpecificationId);
        setApiSpecification(data);
    }

    const handleChildChange = () => {
        fetchApiSpecification();
    };

    const changeOpen = (isOpen: boolean) => {
        setIsNewDomainMode(isOpen)
    }

    const handleCreateDomainModalOpen = () => {
        setIsNewDomainMode(true)

    }

    const handleCrateApiModalOpen = () => {
        setIsNewApiMode(true)
    }

    const changeCreateApiModalOpen = (isOpen: boolean) => {
        setIsNewApiMode(isOpen)
    }


    useEffect(() => {
        fetchApiSpecification()
    }, [projectId, apiSpecificationId]);

    return ApiSpecification ? (
        <>
            <Box>
                <Box
                    sx={{display: 'flex', margin: '10px', justifyContent: 'end'}}
                >
                    <Chip
                        icon={<AddCircleIcon
                            style={{color: '#4F378B'}}
                            />}
                        label="도메인 추가" variant='outlined'
                        onClick={handleCreateDomainModalOpen}
                    />
                </Box>

                {/* Header */}
                <Box
                    // sx={{border: '2px solid black'}}
                >
                    {ApiSpecification.domains.map((domain) => (
                        <List key={`domain-${domain.domainId}`}>
                            <ListItem
                                sx={{
                                    // border: '2px solid blue',
                                    display: 'flex',
                                    flexDirection: 'column',
                                    justifyContent: 'space-between',
                                }}
                            >
                                <DomainComponent
                                    projectId={projectId}
                                    apiSpecificationId={apiSpecificationId}
                                    domainId={domain.domainId}
                                    name={domain.name}
                                    onChildChange={handleChildChange}
                                />
                                <ApiListComponent
                                    projectId={projectId}
                                    apiSpecificationId={apiSpecificationId}
                                    domainId={domain.domainId}
                                    onChildChange={handleChildChange}
                                    apis={domain.apis}/>
                            </ListItem>
                            <Chip
                                icon={<AddCircleIcon
                                    style={{color: '#4F378B'}}
                                />}
                                label="API 추가" variant='outlined'
                                sx={{ml: 2}}
                                onClick={handleCrateApiModalOpen}
                            />
                            <ApiCreateModal
                                projectId={projectId}
                                apiSpecificationId={apiSpecificationId}
                                domainId={domain.domainId}
                                onChildChange={handleChildChange}
                                open={isNewApiMode}
                                changeOpen={changeCreateApiModalOpen}
                            />
                        </List>
                    ))}

                    <CreateDomainModal
                        projectId={projectId}
                        apiSpecificationId={apiSpecificationId}
                        open={isNewDomainMode}
                        onChildChange={handleChildChange}
                        changeOpen={changeOpen}
                    />


                </Box>
            </Box>
        </>
    ) : null;
}

export default ApiSpecificationComponent;
