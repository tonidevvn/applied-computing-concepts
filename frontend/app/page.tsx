'use client'

import { FoodItemType } from '@/app/types/food'
import React, { useEffect, useState } from 'react'
import {
    Card,
    Row,
    Col,
    PaginationProps,
    Flex,
    Pagination,
    Table,
    Button,
} from 'antd'
import axios from 'axios'
import ProductSearch from '@/app/components/ProductSearch'
import FoodItem from '@/app/components/FoodItem'
import { InvertedIndexType } from '@/app/types/invertedindex'
import InvertedIndex from '@/app/components/InvertedIndex'
import { PageRankingDataType } from '@/app/types/pageranking'
import AppAutoComplete from './components/AppAutoComplete'
import { useAppStore } from '@/stores/app-store-provider'
import { useDebounce } from 'use-debounce'

export default function Products() {
    const [items, setItems] = useState<FoodItemType[]>([])
    const [loading, setLoading] = useState(false)

    const { searchValue, setSearchValue } = useAppStore((state) => state)

    const [page, setPage] = useState(1)
    const [size, setSize] = useState(8)
    const [total, setTotal] = useState(0)

    const { spellCheckOptions, setSpellCheckOptions } = useAppStore(
        (state) => state
    )
    const { invertedIndexData, setInvertedIndexData } = useAppStore(
        (state) => state
    )
    const { pageRankingResult, setPageRankingResult } = useAppStore(
        (state) => state
    )
    const { topSearches, setTopSearches } = useAppStore((state) => state)
    const { recentSearches, setRecentSearches } = useAppStore((state) => state)

    const onFetchProducts = async () => {
        try {
            const response = await axios.get('/api/product', {
                params: {
                    q: searchValue,
                    page: page - 1,
                    size,
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

    const [debouncedSearchValue] = useDebounce(searchValue, 200)

    useEffect(() => {
        const fetchSearchHistoryData = async () => {
            try {
                if (!!debouncedSearchValue) {
                    const response = await axios.get('/api/keyword-search', {
                        params: { q: debouncedSearchValue },
                    })
                    const response2 = await axios.get(
                        '/api/keyword-search/list',
                        {
                            params: { q: 'top' },
                        }
                    )
                    const response3 = await axios.get(
                        '/api/keyword-search/list',
                        {
                            params: { q: 'recent' },
                        }
                    )
                    if (!!response.data) {
                        setTopSearches(response2.data)
                        setRecentSearches(response3.data)
                    }
                }
            } catch (error) {
                console.error('Fetch error:', error)
            }
        }
        fetchSearchHistoryData()
    }, [debouncedSearchValue])

    useEffect(() => {
        onFetchProducts()
    }, [size, page])

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
        setSize(pageSize)
    }
    if (loading) {
        return <div>Loading...</div>
    }

    return (
        <div className='container mx-auto p-4'>
            <div className='p-6 my-4 bg-white rounded-lg shadow-md'>
                <h1 className='text-3xl font-bold mb-4'>Search Bar</h1>
                <div className='flex items-center gap-4'>
                    <AppAutoComplete
                        searchValue={searchValue}
                        setSearchValue={setSearchValue}
                        placeholder='Search food items'
                    />
                    <button
                        onClick={() => {
                            onSearch()
                        }}
                        className='px-4 py-2 bg-blue-500 text-white font-semibold rounded-lg shadow hover:bg-blue-600 transition duration-200'
                    >
                        Search
                    </button>
                </div>
            </div>

            <div className='grid grid-row-4 grid-flow-col gap-4'>
                <div className='row-span-4 col-span-6'>
                    <div className='bg-white rounded-lg p-6 mb-4'>
                        <h1 className='text-3xl font-bold mb-4'>Products</h1>
                        <div className='flex flex-col gap-4 '>
                            <div className='overflow-auto'>
                                <FoodItem items={items} />
                            </div>
                            <div className='flex justify-end'>
                                <div className='mt-4'>
                                    <Pagination
                                        className='inline-block'
                                        current={page}
                                        total={total}
                                        onChange={onPaginationChange}
                                        showSizeChanger
                                        pageSizeOptions={['8', '16', '24']}
                                        pageSize={size}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>

                    {invertedIndexData.length > 0 && (
                        <InvertedIndex data={invertedIndexData} />
                    )}
                </div>
                <div className='col-span-2'>
                    {spellCheckOptions?.length > 0 && (
                        <div className='bg-white rounded-lg p-6 mb-4'>
                            <h1 className='text-3xl font-bold mb-4'>
                                Spell Checking
                            </h1>
                            <div className='gap-4 my-4 items-center row-span-1'>
                                <p className='text-lg md:col-span-1 my-4'>
                                    Do you mean:
                                </p>
                                <div className='grid grid-cols-2 md:grid-cols-3 gap-2 md:col-span-3'>
                                    {spellCheckOptions.map((option, i) => (
                                        <button
                                            key={i}
                                            onClick={() =>
                                                setSearchValue(option)
                                            }
                                            className='bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold py-2 px-4 rounded'
                                        >
                                            {option}
                                        </button>
                                    ))}
                                </div>
                            </div>
                        </div>
                    )}

                    {topSearches?.length > 0 && (
                        <div className='row-span-3 bg-white rounded-lg p-6 mb-4'>
                            <h1 className='text-3xl font-bold mb-4'>
                                Searches History
                            </h1>
                            <Table
                                columns={[
                                    {
                                        dataIndex: 'keyword',
                                        title: 'Keyword',
                                        ellipsis: true,
                                        width: 250,
                                    },
                                    {
                                        dataIndex: 'count',
                                        title: 'Count',
                                        width: 150,
                                    },
                                ]}
                                dataSource={topSearches}
                            />
                        </div>
                    )}

                    {pageRankingResult?.length > 0 && (
                        <div className='row-span-3 bg-white rounded-lg p-6 mb-4'>
                            <h1 className='text-3xl font-bold mb-4'>
                                Page Ranking
                            </h1>
                            <Table
                                columns={[
                                    {
                                        dataIndex: 'url',
                                        title: 'URL',
                                        ellipsis: true,
                                        width: 250,
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
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}
