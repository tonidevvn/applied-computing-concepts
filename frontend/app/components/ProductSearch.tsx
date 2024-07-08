'use client'

import React, {useState} from 'react'
import {Flex, Button} from 'antd'
import AppAutoComplete from "@/app/components/AppAutoComplete";
import AppKeywordSearchHistory from "@/app/components/AppKeywordSearchHistory";
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
                        placeholder='Search food items'
                    />
                    <Button type="primary" icon={<SearchOutlined />} iconPosition={'end'} onClick={() => setQueryValue(searchValue)}>
                        Search
                    </Button>
                </Flex>
                <AppKeywordSearchHistory
                    queryValue={queryValue}  />
            </Flex>
    )
}
