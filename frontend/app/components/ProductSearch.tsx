'use client'

import React, {useState} from 'react'
import {Flex, Button} from 'antd'
import AppAutoComplete from "@/app/components/AppAutoComplete";
import AppSpellChecking from "@/app/components/AppSpellChecking";
import AppSearchHistory from "@/app/components/AppSearchHistory";
import {SearchOutlined} from "@ant-design/icons";


export default function ProductSearch({
                                          searchValue,
                                          setSearchValue,
                                      }: {
    searchValue: string
    setSearchValue: (value: string) => void
}) {

    const [queryValue, setQueryValue] = useState('')

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
                <AppSearchHistory
                    queryValue={queryValue}  />
            </Flex>
    )
}
