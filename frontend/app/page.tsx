'use client'

import { FoodItemType } from '@/app/types/food'
import React, { useEffect, useState } from 'react'
import { Card, Row, Col, PaginationProps, Flex, Pagination, Table } from 'antd'
import axios from 'axios'
import ProductSearch from '@/app/components/ProductSearch'
import FoodItem from '@/app/components/FoodItem'
import { InvertedIndexType } from '@/app/types/invertedindex'
import InvertedIndex from '@/app/components/InvertedIndex'
import { PageRankingDataType } from '@/app/types/pageranking'
import FrequencyCount from './components/FrequencyCount'
export default function Products() {
    const [items, setItems] = useState<FoodItemType[]>([])
    const [loading, setLoading] = useState(false)
    const [searchValue, setSearchValue] = useState('')
    const [page, setPage] = useState(1)
    const [limit, setLimit] = useState(10)
    const [total, setTotal] = useState(0)
    const [spellCheckOptions, setSpellCheckOptions] = useState<string[]>([])
    const [invertedIndexData, setInvertedIndexData] = useState<
        InvertedIndexType[]
    >([])
    const [pageRankingResult, setPageRankingResult] = useState<
        PageRankingDataType[]
    >([])

    const onFetchProducts = async () => {
        try {
            const response = await axios.get('/api/product', {
                params: {
                    q: searchValue,
                    page: page - 1,
                    limit,
                },
            })
            setItems(response.data.content)
            setTotal(response.data.totalElements)
        } catch (error) {
            console.error('Fetch error:', error)
        } finally {
            setLoading(false)
        }
    }

    useEffect(() => {
        onFetchProducts()
    }, [limit, page])

    const onSearch = async () => {
        setLoading(true)
        await Promise.all([
            onFetchProducts(),
            onSpellCheck(),
            onCheckInvertedIndex(),
            onFetchPageRankingData(),
        ]).then(() => {
            setLoading(false)
        })
    }

    const onSpellCheck = async () => {
        try {
            const response = await axios.get('/api/spell-checking', {
                params: { word: searchValue },
            })
            setSpellCheckOptions(response.data)
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const onCheckInvertedIndex = async () => {
        try {
            const response = await axios.get('/api/inverted-index', {
                params: { query: searchValue },
            })
            setInvertedIndexData(response.data)
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const onFetchPageRankingData = async () => {
        try {
            setPageRankingResult([])
            const response = await axios.get('/api/page-ranking', {
                params: { search: searchValue },
            })

            setPageRankingResult(response.data)
        } catch (error) {
            console.error('Fetch error:', error)
        }
    }

    const onPaginationChange: PaginationProps['onShowSizeChange'] = (
        current,
        pageSize
    ) => {
        setPage(current)
        setLimit(pageSize)
    }
    if (loading) {
        return <div>Loading...</div>
    }

    return (
        <div className='container mx-auto p-4'>
            <div className='grid grid-cols-4 md:grid-cols-6 gap-4'>
                <div className='col-span-4'>
                    <ProductSearch
                        searchValue={searchValue}
                        setSearchValue={setSearchValue}
                        onSearch={onSearch}
                    />

                    <div className='flex flex-col gap-4'>
                        <FoodItem items={items} />
                        <div className='flex justify-end'>
                            <div className='mt-4'>
                                <Pagination
                                    className='inline-block'
                                    current={page}
                                    total={total}
                                    onChange={onPaginationChange}
                                    showSizeChanger
                                />
                            </div>
                        </div>
                    </div>

                    {invertedIndexData.length > 0 && (
                        <InvertedIndex data={invertedIndexData} />
                    )}
                </div>
                <div className='grid grid-rows-4 col-span-auto'>
                    {spellCheckOptions.length > 0 && (
                        <div className='gap-4 my-4 items-center row-span-1'>
                            <p className='text-lg md:col-span-1 my-4'>
                                Do you mean:
                            </p>
                            <div className='grid grid-cols-2 md:grid-cols-3 gap-2 md:col-span-3'>
                                {spellCheckOptions.map((option, i) => (
                                    <button
                                        key={i}
                                        onClick={() => setSearchValue(option)}
                                        className='bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold py-2 px-4 rounded'
                                    >
                                        {option}
                                    </button>
                                ))}
                            </div>
                        </div>
                    )}
                    {pageRankingResult.length > 0 && (
                        <div className='row-span-3'>
                            <p>Ranking page</p>
                            <Table
                                columns={[
                                    {
                                        dataIndex: 'url',
                                        title: 'URL',
                                        ellipsis: true,
                                        width: 300,
                                        render: (url: string) => (
                                            <a
                                                href={url}
                                                target='_blank'
                                                title={url}
                                            >
                                                {url}
                                            </a>
                                        ),
                                    },
                                    {
                                        dataIndex: 'frequencyOfSearchKeyword',
                                        title: 'Frequency',
                                        width: 150,
                                    },
                                ]}
                                dataSource={pageRankingResult}
                            />
                            <FrequencyCount />
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}
