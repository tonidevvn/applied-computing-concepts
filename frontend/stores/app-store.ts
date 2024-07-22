import { createStore } from 'zustand/vanilla'
import {PageRankingDataType} from "@/app/types/pageranking";
import {InvertedIndexType} from "@/app/types/invertedindex";
import {SearchDataType} from "@/app/types/searchdata";

export type AppState = {
    searchValue: string
    pageRankingResult: PageRankingDataType[]
    spellCheckOptions: string[]
    invertedIndexData: InvertedIndexType[]
    topSearches: SearchDataType[],
    recentSearches: SearchDataType[]
}

export type AppStateActions = {
    setSearchValue: (searchValue: string) => void
    setPageRankingResult: (pageRankingResult: PageRankingDataType[]) => void
    setSpellCheckOptions: (spellCheckOptions: string[]) => void
    setInvertedIndexData: (invertedIndex: InvertedIndexType[]) => void
    setTopSearches: (topSearches: SearchDataType[]) => void
    setRecentSearches: (topSearches: SearchDataType[]) => void
}

export type AppStore = AppState & AppStateActions

export const defaultInitState: AppState = {
    searchValue: '',
    pageRankingResult: [],
    spellCheckOptions: [],
    invertedIndexData: [],
    topSearches: [],
    recentSearches: []
}

export const createAppStore = (
    initState: AppState = defaultInitState,
) => {
    return createStore<AppStore>()((set) => ({
        ...initState,
        setSearchValue: (searchValue: string) => set((state) => ({ searchValue })),
        setPageRankingResult: (pageRankingResult: PageRankingDataType[]) => set ((state) => ({ pageRankingResult})),
        setSpellCheckOptions: (spellCheckOptions: string[]) => set ((state) => ({ spellCheckOptions})),
        setInvertedIndexData: (invertedIndexData: InvertedIndexType[]) => set ((state) => ({ invertedIndexData})),
        setTopSearches: (topSearches: SearchDataType[]) => set ((state) => ({ topSearches})),
        setRecentSearches: (recentSearches: SearchDataType[]) => set ((state) => ({ recentSearches})),
    }))
}