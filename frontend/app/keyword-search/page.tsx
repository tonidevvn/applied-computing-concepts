'use client'
import {Button, Card, Flex, Input, Space, Switch} from "antd";
import AppAutoComplete from "@/app/components/AppAutoComplete";
import {CheckOutlined, CloseOutlined, SearchOutlined} from "@ant-design/icons";
import AppKeywordSearchHistory from "@/app/components/AppKeywordSearchHistory";
import React, {useState} from "react";
import axios from "axios";
import {SearchDataType} from "@/app/types/searchdata";
import {PageRankingDataType} from "@/app/types/pageranking";

export default function Page() {

    const [searchValue, setSearchValue] = useState('')
    const [queryValue, setQueryValue] = useState('')
    const [urlSearch, setUrlSearch] = useState(false)
    const [urlToBeSearched, setUrlToBeSearched] = useState('')
    const [searchResult, setSearchResult] = useState<Nullable<SearchDataType>>(null)
    const [loading, setLoading] = useState(false)
    const [pageRankingResult, setPageRankingResult] = useState<PageRankingDataType[]>([])

    const onChange = (checked: boolean) => {
        console.log(`switch to ${checked}`)
        setUrlSearch(checked)
    };

    const loadDemo1 = () => {
        setUrlSearch(false)
        setSearchValue("sauce")
        setUrlToBeSearched("")
    };

    const loadDemo2 = () => {
        setUrlSearch(true)
        setSearchValue("chicken")
        setUrlToBeSearched("http://www.multifoodwindsor.com/ecommerce/catalog/category/view/s/food-court%E9%A4%90%E9%A5%AE/id/69/")
    };

    const fetchKeywordSearchData = async () => {
        try {
            setSearchResult(null)
            const response = await axios.get('/api/keyword-search/frequency', {
                params: { q: searchValue , url: urlToBeSearched},
            })

            console.info(response.data)
            setSearchResult(
                response.data
            )
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const fetchPageRankingData = async () => {
        try {
            setPageRankingResult([])
            const response = await axios.get('/api/page-ranking', {
                params: { search: searchValue },
            })

            console.info(response.data)
            setPageRankingResult(
                response.data
            )
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const handleKeywordSearch = async() => {
        setLoading(true)
        setPageRankingResult([])
        setSearchResult(null)

        if (urlSearch) {
            await fetchKeywordSearchData()
        } else {
            await fetchPageRankingData()
        }
        setLoading(false)
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
                <Button type="primary" icon={<SearchOutlined/>} iconPosition={'end'} onClick={handleKeywordSearch}
                        loading={loading}>
                    Search
                </Button>
            </Flex>

            <Space direction="horizontal" size={"middle"}>
            <a href='#' onClick={loadDemo1}>Demo 1</a>
            <a href='#' onClick={loadDemo2}>Demo 2</a>
            </Space>

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
                    <Card title={`Search results for the keyword ‘${searchResult?.keyword}’`} bordered={false}
                          style={{width: 360}}>
                        <p><b>Keyword</b>: <span className="bg-yellow-200 px-2 ms-1">{searchResult?.keyword}</span>
                        </p>
                        <p><b>URL</b>: <a href={searchResult?.url} target={"_blank"} title={searchResult?.url}
                                          className="ms-1">👉👉👉 🔗 Hover me </a> </p>
                        <p><b>Frequency</b>:
                                <a href='#'
                                   title={`Frequency of the term ‘${searchResult?.keyword}’ appearing in URL(s)`}
                                   className="bg-yellow-200 px-2 ms-1">
                                    {searchResult?.frequency}
                                </a>
                        </p>
                    </Card>
                )
                : <></>
                )
            }
            {
                (pageRankingResult && pageRankingResult.length ? (
                        <>
                            <p>Top search results for ‘{searchValue}’</p>
                            {pageRankingResult.map((pr, index) => (
                                <Card bordered={true}
                                      style={{width: 360}}>
                                    <p><b>#{index + 1}</b> <a href={pr?.url} target={"_blank"} title={pr?.url}
                                                              className="ms-1">👉👉👉 🔗 Hover me </a>
                                    </p>
                                    <p><b>Frequency</b>:
                                        <a href='#'
                                           title={`Frequency of the term ‘${pr?.keyword}’ appearing in URL(s)`}
                                           className="bg-yellow-200 px-2 ms-1">
                                            {pr?.frequencyOfSearchKeyword}
                                        </a>
                                    </p>
                                </Card>
                            ))
                            }
                        </>
                    ) : <></>
                )
            }

        </Flex>
    )
}