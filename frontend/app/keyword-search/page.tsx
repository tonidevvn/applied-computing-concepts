'use client'
import {Button, Card, Flex, Input, Space, Switch} from "antd";
import AppAutoComplete from "@/app/components/AppAutoComplete";
import {CheckOutlined, CloseOutlined, SearchOutlined} from "@ant-design/icons";
import AppKeywordSearchHistory from "@/app/components/AppKeywordSearchHistory";
import React, {useState} from "react";
import axios from "axios";
import {SearchDataType} from "@/app/types/searchdata";

export default function Page() {

    const [searchValue, setSearchValue] = useState('')
    const [queryValue, setQueryValue] = useState('')
    const [urlSearch, setUrlSearch] = useState(false)
    const [urlToBeSearched, setUrlToBeSearched] = useState('')
    const [searchResult, setSearchResult] = useState<Nullable<SearchDataType>>(null)
    const [loading, setLoading] = useState(false)

    const onChange = (checked: boolean) => {
        console.log(`switch to ${checked}`)
        setUrlSearch(checked)
    };

    const loadDemo = () => {
        setUrlSearch(true)
        setSearchValue("chicken")
        setUrlToBeSearched("http://www.multifoodwindsor.com/ecommerce/catalog/category/view/s/food-court%E9%A4%90%E9%A5%AE/id/69/")
    };

    const fetchKeywordSearchData = async () => {
        try {
            setLoading(true)
            setSearchResult(null)
            const response = await axios.get('/api/keyword-search/frequency', {
                params: { q: searchValue , url: urlToBeSearched},
            })

            console.info(response.data)
            setSearchResult(
                response.data
            )
            setLoading(false)
        } catch (error) {
            console.error('Fetch error:', error)
            setLoading(false)
        }
    }

    return (
        <Flex vertical={true} gap={'middle'}>
            <h2>Keyword Search & Page ranking</h2>
            <Flex vertical={false} gap={'middle'}>
                <AppAutoComplete
                    searchValue={searchValue}
                    setSearchValue={setSearchValue}
                    placeholder='Search food items'
                />
                <Button type="primary" icon={<SearchOutlined/>} iconPosition={'end'} onClick={fetchKeywordSearchData}
                        loading={loading}>
                    Search
                </Button>
            </Flex>

            <a href='#' onClick={loadDemo}>Demo 1</a>

            <Space direction="horizontal">
                Want to search in a specific URL?
                <Switch
                    checkedChildren={<CheckOutlined/>}
                    unCheckedChildren={<CloseOutlined/>}
                    defaultChecked={false}
                    checked={urlSearch} onChange={onChange}
                />
            </Space>
            {urlSearch && (
                <Flex vertical={false}>
                    <Space.Compact style={{width: '360px'}}>
                        <Input placeholder="input search URL" value={urlToBeSearched}
                               onChange={e => setUrlToBeSearched(e.target.value)}/>
                    </Space.Compact>
                </Flex>
            )}

            <AppKeywordSearchHistory
                queryValue={queryValue}/>


            {
                (searchResult ? (

                            <Card title={`Search results for the keyword ‘${searchResult.keyword}’`} bordered={false}
                                  style={{width: 360}}>
                                <p><b>Keyword</b>: <span className="bg-yellow-200 px-2 ms-1">{searchResult.keyword}</span>
                                </p>
                                <p><b>URL</b>: <a href={searchResult.url} target={"_blank"} title={searchResult.url}
                                                  className="ms-1">Hover me</a></p>
                                <p><b>Frequency</b>:
                                    <a href='#'
                                       title={`Frequency of the term ‘${searchResult.keyword}’ appearing in URL(s)`}
                                       className="bg-yellow-200 px-2 ms-1">
                                        {searchResult.frequency}
                                    </a>
                                </p>
                            </Card>
                        )
                        : <></>
                )
            }

        </Flex>
    )
}