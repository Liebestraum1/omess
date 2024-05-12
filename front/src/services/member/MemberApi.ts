import client from "../common";

export const findMemberByEmail = async (email: string) => {
    return await client.get(`/api/v1/members?email=${email}`)
        .then(response => response.data)
        .catch(() => []);
}

export const findMemberById = async (memberId: number) => {
    return await client.get(`/api/v1/members?memberId=${memberId}`)
        .then(response => response.data)
        .catch(() => []);
}