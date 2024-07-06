'use client'
import {Button, Flex} from "antd";
import AppAutoComplete from "@/app/components/AppAutoComplete";
import {SearchOutlined} from "@ant-design/icons";
import AppSpellChecking from "@/app/components/AppSpellChecking";
import AppSearchHistory from "@/app/components/AppSearchHistory";
import React, {useState} from "react";
import {useDebounce} from "use-debounce";

export default function Page() {

    const [searchValue, setSearchValue] = useState('')
    const [queryValue, setQueryValue] = useState('')
    const [debounced ] = useDebounce(searchValue, 1000);

    return (
        <Flex vertical={true} gap={'middle'}>
            <Flex vertical={false} gap={'middle'} >
                <AppAutoComplete
                    searchValue={searchValue}
                    setSearchValue={setSearchValue}
                />
                <Button type="primary" icon={<SearchOutlined />} iconPosition={'end'} onClick={() => setQueryValue(searchValue)}>
                    Search
                </Button>
            </Flex>
            <AppSpellChecking
                searchValue={debounced}
                setSearchValue={setSearchValue}
            />
            <AppSearchHistory
                queryValue={queryValue}  />
        </Flex>
    )
}